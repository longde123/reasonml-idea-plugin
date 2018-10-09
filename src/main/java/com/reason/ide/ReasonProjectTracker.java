package com.reason.ide;

import com.intellij.AppTopics;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.messages.MessageBusConnection;
import com.reason.hints.InsightManagerImpl;
import com.reason.ide.format.ReformatOnSave;
import com.reason.ide.hints.RmlDocumentListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static com.reason.Platform.getOsPrefix;

public class ReasonProjectTracker implements ProjectComponent {

    private final Logger m_log = Logger.getInstance("ReasonML");

    private final Project m_project;
    @Nullable
    private RmlDocumentListener m_documentListener;
    @Nullable
    private MessageBusConnection m_messageBusConnection;
    @Nullable
    private VirtualFileListener m_vfListener;
    @Nullable
    private RmlFileEditorListener m_fileEditorListener;

    protected ReasonProjectTracker(Project project) {
        m_project = project;
    }

    @Override
    public void projectOpened() {
        if (SystemInfo.is64Bit) {
            // Try to locate Rincewind
            if (!getOsPrefix().isEmpty()) {
                InsightManagerImpl insightManager = (InsightManagerImpl) InsightManagerImpl.getInstance(m_project);
                File rincewindFile = insightManager.getRincewindFile();
                if (rincewindFile.exists()) {
                    m_log.info("Found " + rincewindFile);
                    insightManager.isDownloaded.set(true);
                } else {
                    insightManager.downloadRincewindIfNeeded();
                }
            }
        } else {
            m_log.info("32Bit system detected, can't use rincewind");
        }

        m_documentListener = new RmlDocumentListener(m_project);
        EditorFactory.getInstance().getEventMulticaster().addDocumentListener(m_documentListener);

        m_messageBusConnection = m_project.getMessageBus().connect();
        m_fileEditorListener = new RmlFileEditorListener(m_project);
        m_messageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, m_fileEditorListener);
        m_messageBusConnection.subscribe(AppTopics.FILE_DOCUMENT_SYNC, new ReformatOnSave(m_project));

        m_vfListener = new VirtualFileListener(m_project);
        VirtualFileManager.getInstance().addVirtualFileListener(m_vfListener);
    }

    @Override
    public void projectClosed() {
        if (m_documentListener != null) {
            EditorFactory.getInstance().getEventMulticaster().removeDocumentListener(m_documentListener);
            m_documentListener = null;
        }
        if (m_vfListener != null) {
            VirtualFileManager.getInstance().removeVirtualFileListener(m_vfListener);
            m_vfListener = null;
        }
        if (m_messageBusConnection != null) {
            m_messageBusConnection.disconnect();
            m_messageBusConnection = null;
        }
    }

    public boolean isOpen(@NotNull VirtualFile file) {
        return m_fileEditorListener != null && m_fileEditorListener.isOpen(file);
    }
}
