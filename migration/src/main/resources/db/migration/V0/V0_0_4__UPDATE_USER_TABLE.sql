ALTER TABLE [USER] ADD IS_ENABLED BIT DEFAULT '1' WITH VALUES;

ALTER TABLE [USER] ADD IS_DELETED BIT DEFAULT '0' WITH VALUES;

ALTER TABLE [USER] ADD IS_EDITABLE BIT DEFAULT '1' WITH VALUES;