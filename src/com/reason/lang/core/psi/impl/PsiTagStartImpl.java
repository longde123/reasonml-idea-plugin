package com.reason.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiQualifiedNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.reason.ide.files.FileBase;
import com.reason.lang.core.HMSignature;
import com.reason.lang.core.ORUtil;
import com.reason.lang.core.PsiFileHelper;
import com.reason.lang.core.PsiFinder;
import com.reason.lang.core.psi.*;
import com.reason.lang.core.type.ORTypes;
import com.reason.lang.reason.RmlTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PsiTagStartImpl extends PsiToken<ORTypes> implements PsiTagStart {
    public PsiTagStartImpl(@NotNull ASTNode node) {
        super(RmlTypes.INSTANCE, node);
    }

    static class TagPropertyImpl implements TagProperty {

        private final String m_name;
        private final String m_type;
        private boolean m_mandatory;

        TagPropertyImpl(PsiRecordField field, List<PsiAnnotation> annotations) {
            m_name = field.getName();
            PsiSignature signature = field.getSignature();
            HMSignature hmSignature = signature == null ? HMSignature.EMPTY : signature.asHMSignature();
            m_type = hmSignature.toString();
            m_mandatory = hmSignature.isMandatory(0);

            for (PsiAnnotation annotation : annotations) {
                if ("@bs.optional".equals(annotation.getName())) {
                    m_mandatory = false;
                }
            }
        }

        TagPropertyImpl(PsiParameter parameter) {
            m_name = parameter.getName();
            PsiSignature signature = parameter.getSignature();
            HMSignature hmSignature = signature == null ? HMSignature.EMPTY : signature.asHMSignature();
            m_type = hmSignature.toString();
            m_mandatory = !parameter.hasDefaultValue() && hmSignature.isMandatory(0);
        }

        TagPropertyImpl(String name, String type, boolean mandatory) {
            m_name = name;
            m_type = type;
            m_mandatory = mandatory;
        }

        @Override
        public String getName() {
            return m_name;
        }

        @Override
        public String getType() {
            return m_type;
        }

        @Override
        public boolean isMandatory() {
            return m_mandatory;
        }

        @Override
        public String toString() {
            return m_name + ":" + m_type;
        }
    }

    public static TagProperty createProp(String name, String type) {
        return new TagPropertyImpl(name, type, false);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return getFirstChild().getNextSibling();
    }

    @Nullable
    @Override
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return nameIdentifier == null ? null : nameIdentifier.getText();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @NotNull
    @Override
    public List<PsiTagProperty> getProperties() {
        return ORUtil.findImmediateChildrenOfClass(this, PsiTagProperty.class);
    }

    @Override
    public List<TagProperty> getUnifiedPropertyList() {
        final List<TagProperty> result = new ArrayList<>();

        PsiFinder psiFinder = PsiFinder.getInstance();
        Project project = getProject();

        // find tag 'make' expression
        PsiElement tagName = findChildByClass(PsiUpperSymbol.class);
        if (tagName == null) {
            // no tag name, it's not a custom tag
            tagName = findChildByClass(PsiLowerSymbol.class);
            if (tagName != null) {
                FileBase reactDOMRe = psiFinder.findFileModule(project, "ReactDOMRe");
                if (reactDOMRe != null) {
                    PsiNamedElement props = reactDOMRe.getTypeExpression("props");
                    if (props != null) {
                        PsiTypeBinding binding = PsiTreeUtil.getStubChildOfType(props, PsiTypeBinding.class);
                        if (binding != null) {
                            PsiRecord record = PsiTreeUtil.getStubChildOfType(binding, PsiRecord.class);
                            if (record != null) {
                                for (PsiRecordField field : record.getFields()) {
                                    result.add(new TagPropertyImpl(field, ORUtil.prevAnnotations(field)));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // The tag is a custom component
            PsiQualifiedNamedElement module = psiFinder.findModuleFromQn(project, tagName.getText());
            if (module == null) {
                // If nothing found, look for an inner module in current file
                String fileModuleName = ((FileBase) tagName.getContainingFile()).asModuleName();
                module = psiFinder.findComponent(fileModuleName + "." + tagName.getText(), project, GlobalSearchScope.allScope(project));
            }

            if (module != null) {
                Collection<PsiLet> expressions = (module instanceof FileBase) ? PsiFileHelper.getLetExpressions((PsiFile) module) : ((PsiModule) module).getLetExpressions();
                for (PsiLet expression : expressions) {
                    if ("make".equals(expression.getName())) {
                        PsiFunction function = expression.getFunction();
                        if (function != null) {
                            function.getParameterList().
                                    stream().
                                    filter(p -> !"children".equals(p.getName()) && !"_children".equals(p.getName())).
                                    forEach(p -> result.add(new TagPropertyImpl(p)));
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "Tag start";
    }
}
