package com.reason;

import javax.swing.*;
import org.jetbrains.annotations.NotNull;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.reason.ide.files.RmlFileType;
import com.reason.lang.RmlLanguage;
import com.reason.lang.core.psi.Module;
import com.reason.lang.core.psi.impl.ModuleImpl;

public class RmlFile extends PsiFileBase {
    public RmlFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, RmlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return RmlFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Reason File";
    }

    public Module[] getModules() {
        return findChildrenByClass(Module.class);
    }

    public Module getModule(String name) {
        Module[] modules = getModules();
        for (Module module : modules) {
            if (name.equals(module.getName())) {
                return module;
            }
        }
        return null;
    }
}
