package com.reason.ide.docs;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.reason.lang.core.HMSignature;
import com.reason.lang.core.psi.PsiUpperSymbol;
import com.reason.lang.core.psi.PsiVal;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DocumentationProvider extends AbstractDocumentationProvider {

    public static final Key<Map<LogicalPosition, HMSignature>> SIGNATURE_CONTEXT = Key.create("REASONML_SIGNATURE_CONTEXT");

    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        if (element instanceof PsiUpperSymbol) {
            element = element.getParent();
            PsiElement previousElement = element == null ? null : PsiTreeUtil.prevVisibleLeaf(element);
            if (previousElement instanceof PsiComment) {
                return previousElement.getText();
            }
        } else if (element instanceof PsiVal) {
            PsiElement previousElement = PsiTreeUtil.prevVisibleLeaf(element);
            if (previousElement instanceof PsiComment) {
                String commentText = previousElement.getText();
                return commentText.substring(3/* (** */, commentText.length() - 2 /* *) */).trim();
            }
        }

        return super.generateDoc(element, originalElement);
    }

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        PsiFile psiFile = originalElement.getContainingFile();
        TextEditor editor = (TextEditor) FileEditorManager.getInstance(originalElement.getProject()).getSelectedEditor(psiFile.getVirtualFile());

        if (editor != null) {
            LogicalPosition elementPosition = editor.getEditor().offsetToLogicalPosition(originalElement.getTextOffset());
            Map<LogicalPosition, HMSignature> signatures = psiFile.getUserData(SIGNATURE_CONTEXT);
            if (signatures != null) {
                HMSignature signature = signatures.get(elementPosition);
                if (signature != null) {
                    return signature.toString();
                }
            }
        }

        return super.getQuickNavigateInfo(element, originalElement);
    }
}
