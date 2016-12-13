package com.goeuro.busroute.listener;

import java.io.File;
import java.nio.file.Paths;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.goeuro.busroute.listener.event.RestartEvent;

/**
 * A simple file watcher
 * 
 * @author Abhilash Ghosh
 *
 */
@Component
public class FileChangeComponent implements ApplicationListener<ContextRefreshedEvent> {
	public static final Logger LOG = LoggerFactory.getLogger(FileChangeComponent.class);

	private FileSystemWatcher fileWatcher;

	private final ApplicationEventPublisher publisher;

	@Value("${file.name}")
	private String fileName;

	@Autowired
	public FileChangeComponent(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		fileWatcher = new FileSystemWatcher(true, 5, 3);

		File file = new File(fileName);
		File directory = Paths.get(file.getAbsolutePath()).getParent().toFile();

		fileWatcher.addSourceFolder(directory);

		fileWatcher.addListener(changeSet -> processChangeSet(changeSet, directory, file));

		fileWatcher.start();

	}

	private void processChangeSet(Set<ChangedFiles> changeSet, File directory, File file) {

		boolean fileChangeMatched = changeSet
				.stream()
				.flatMap(changedFiles -> changedFiles.getFiles().stream())
				.anyMatch(changedFile -> changedFile.getFile().getName().equalsIgnoreCase(file.getName()));

		if (fileChangeMatched) {
			this.publisher.publishEvent(new RestartEvent());
		}
	}

}
