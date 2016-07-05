package com.notnoop.apns.internal;

import com.notnoop.apns.ApnsNotification;
import com.notnoop.exceptions.NetworkIOException;

public class BatchApnsService extends AbstractApnsService {
	private ApnsConnection prototype;

	// private Queue<ApnsNotification> batch = new
	// ConcurrentLinkedQueue<ApnsNotification>();

	// private ScheduledExecutorService scheduleService;
	// private ScheduledFuture<?> taskFuture;

	// private Runnable batchRunner = new SendMessagessBatch();

	public BatchApnsService(ApnsConnection prototype, ApnsFeedbackConnection feedback) {
		super(feedback);
		this.prototype = prototype;
	}

	public void start() {
		// no code
	}

	public void stop() {
		Utilities.close(prototype);
	}

	@Override
	public void push(ApnsNotification message) throws NetworkIOException {
		prototype.copy().sendMessage(message);
	}

	public void testConnection() throws NetworkIOException {
		// TODO Auto-generated method stub
		prototype.testConnection();
	}
}
