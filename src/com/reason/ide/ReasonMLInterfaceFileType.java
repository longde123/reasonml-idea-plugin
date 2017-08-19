package com.reason.ide;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.reason.icons.ReasonMLIcons;
import com.reason.lang.RmlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ReasonMLInterfaceFileType extends LanguageFileType {
    public static final ReasonMLInterfaceFileType INSTANCE = new ReasonMLInterfaceFileType();

    private ReasonMLInterfaceFileType() {
        super(RmlLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Reason interface file";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Reason language interface file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "rei";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return ReasonMLIcons.INTERFACE_FILE;
    }}
