package com.reason.lang.core;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.reason.ide.files.FileBase;
import com.reason.lang.core.psi.PsiLet;
import com.reason.lang.core.psi.PsiModule;
import com.reason.lang.core.psi.PsiType;
import com.reason.lang.reason.RmlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RmlElementFactory {
    private RmlElementFactory() {
    }

    @Nullable
    public static PsiElement createModuleName(Project project, String name) {
        FileBase file = createFileFromText(project, "module " + name + " = {};");
        return ((PsiModule) file.getFirstChild()).getNameIdentifier();
    }

    @Nullable
    public static PsiElement createLetName(Project project, String name) {
        FileBase file = createFileFromText(project, "let " + name + " = 1;");
        return ((PsiLet) file.getFirstChild()).getNameIdentifier();
    }

    public static PsiElement createTypeName(Project project, String name) {
        FileBase dummyFile = createFileFromText(project, "type " + name + ";");
        return ((PsiType) dummyFile.getFirstChild()).getNameIdentifier();
    }

    @Nullable
    public static PsiElement createExpression(Project project, String expression) {
        FileBase file = createFileFromText(project, expression);
        return file.getFirstChild();
    }

    @NotNull
    private static FileBase createFileFromText(@NotNull Project project, @NotNull String text) {
        return (FileBase) PsiFileFactory.getInstance(project).createFileFromText("Dummy.re", RmlLanguage.INSTANCE, text);
    }
}
