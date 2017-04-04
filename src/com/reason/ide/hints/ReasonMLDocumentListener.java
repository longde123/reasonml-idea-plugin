package com.reason.ide.hints;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.reason.psi.ReasonMLLetStatement;
import com.reason.psi.ReasonMLValueName;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ReasonMLDocumentListener implements DocumentListener {

    private final Subject<DocumentEvent> documentEventStream;
    private Disposable subscriber;

    ReasonMLDocumentListener(Project project) {
        this.documentEventStream = PublishSubject.create();

        subscriber = this.documentEventStream.
                debounce(150, TimeUnit.MILLISECONDS).
                subscribe(event -> EventQueue.invokeLater(() -> {
                    Editor selectedTextEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                    if (selectedTextEditor != null) {
                        Document document = selectedTextEditor.getDocument();
                        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
                        if (psiFile != null) {
                            Collection<ReasonMLLetStatement> letStatements = PsiTreeUtil.findChildrenOfType(psiFile, ReasonMLLetStatement.class);
                            List<LogicalPosition> positions = letStatements.stream().map(letStatement -> {
                                // Found a let statement, try to get its type
                                ReasonMLValueName valueName = letStatement.getLetBinding().getValueName();
                                int nameOffset = valueName.getTextOffset();
                                return selectedTextEditor.offsetToLogicalPosition(nameOffset);
                            }).collect(Collectors.toList());

                            if (!positions.isEmpty()) {
                                MerlinQueryTypesTask merlinTask = new MerlinQueryTypesTask(psiFile, letStatements, positions);
                                ApplicationManager.getApplication().executeOnPooledThread(merlinTask); // Let statement has been modified
                            }
                        }
                    }
                }));
    }

    @Override
    public void beforeDocumentChange(DocumentEvent event) {
    }

    @Override
    public void documentChanged(DocumentEvent event) {
        this.documentEventStream.onNext(event);
    }

    void projectClosed() {
        this.subscriber.dispose();
    }

}
