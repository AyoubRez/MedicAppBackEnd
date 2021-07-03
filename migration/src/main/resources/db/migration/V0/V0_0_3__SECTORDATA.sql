USE [medicdb]
GO

INSERT INTO [dbo].[SECTOR]
           ([ADD_TIME]
           ,[SECTOR_CODE]
           ,[SECTOR_NAME]
           ,[EDIT_TIME]
           ,[IS_DELETED]
           ,[IS_ENABLED])
     VALUES
           (GETDATE()
           ,'505120'
           ,'ADMINISTATION'
           ,GETDATE()
           ,'false'
           ,'true'),

		   (GETDATE()
           ,'505400'
           ,'DOCTOR'
           ,GETDATE()
           ,'false'
           ,'true'),

		   (GETDATE()
           ,'505500'
           ,'SECRETARY'
           ,GETDATE()
           ,'false'
           ,'true'),

		   (GETDATE()
           ,'505600'
           ,'DXCARE'
           ,GETDATE()
           ,'false'
           ,'true')
GO