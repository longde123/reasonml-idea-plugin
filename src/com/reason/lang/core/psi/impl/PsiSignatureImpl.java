package com.reason.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.reason.ide.files.FileHelper;
import com.reason.lang.core.HMSignature;
import com.reason.lang.core.psi.PsiSignature;
import com.reason.lang.core.psi.PsiSignatureItem;
import com.reason.lang.core.type.ORTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PsiSignatureImpl extends PsiToken<ORTypes> implements PsiSignature {

    public PsiSignatureImpl(@NotNull ORTypes types, @NotNull ASTNode node) {
        super(types, node);
    }

    @NotNull
    @Override
    public HMSignature asHMSignature() {
        boolean isOcaml = FileHelper.isOCaml(getContainingFile().getFileType());
        Collection<PsiSignatureItem> items = PsiTreeUtil.findChildrenOfType(this, PsiSignatureItemImpl.class);
        return new HMSignature(isOcaml, items);
    }

    @NotNull
    @Override
    public String asString() {
        return asHMSignature().toString();
    }

    @Override
    public String toString() {
        return "Signature";
    }
}
