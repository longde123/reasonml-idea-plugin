package com.reason.lang.core.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.reason.lang.core.ORUtil;
import com.reason.lang.core.psi.PsiSignatureItem;
import com.reason.lang.core.type.ORTypes;
import org.jetbrains.annotations.NotNull;

public class PsiSignatureItemImpl extends PsiToken<ORTypes> implements PsiSignatureItem {
    public PsiSignatureItemImpl(@NotNull ORTypes types, @NotNull ASTNode node) {
        super(types, node);
    }

    @Override
    public boolean isNamedItem() {
        PsiElement firstChild = getFirstChild();
        return firstChild != null && ORUtil.nextSiblingWithTokenType(firstChild, m_types.COLON) != null;
    }

    @Override
    public String toString() {
        return "Signature item";
    }
}
