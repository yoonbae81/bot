-- :name create-monitor-table
-- :command :execute
-- :result :raw
CREATE TABLE `monitor` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`chat_id`	TEXT NOT NULL,
	`symbol`	TEXT NOT NULL,
	`compare`	TEXT NOT NULL,
	`price`	INTEGER NOT NULL,
	`created_at`	TIMESTAMP NOT NULL DEFAULT (DATETIME('now','localtime')),
	`notified_at`	TIMESTAMP
);
