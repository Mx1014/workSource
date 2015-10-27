#
# Special notes about the schema design below (KY)
#
# Custom fileds
# 	To balance performance and flexibility, some tables carry general purpose integer fields and string fields,
# 	interpretation of these fields will be determined by the applications on top of, at database level, we only
# 	provide general indexing support for these fields, it is the responsibility of the application to map queries that
# 	are against to these fields.
#
# 	Initially, only two of integral-type and string-type fields are indexed, more indices can be added during operating
# 	time, tuning changes about the indexing will be sync-ed back into schema design afterwards
#
# namespaces and application modules
#	Reusable modules are abstracted under the concept of application. The platform provides built-in application modules
#	such as messaging application module, forum application module, etc. These built-in application modules are running 
#   in the context of core server. When a application module has external counterpart at third-party servers or remote client endpoints, 
#	the API it provides requires to go through the authentication system via appkey/secret key pair mechanism
#
#   Namespace is used to put related resources into distinct domains
#
# namespace and application design rules
#	Shared resources (usually system defined) that are common to all namespaces do not need namespace_id field
#	First level resources usually have namespace_id field
#	Secondary level resources do not need namespace_id field
#	objects that can carry information generated from multiple application modules usualy have app_id field
#	all profile items have app_id field, so that it allows other application modules to attach application specific
#	profile information into it
#
# name convention
#	index prefix: i_eh_
#	unique index prefix: u_eh_
#	foreign key constraint prefix: fk_eh_
# 	table prefix: eh_
#
# record deletion
# 	There are two deletion policies in regards to deletion
#		mark-deletion: mark it as deleted, wait for lazy cleanup or archive
#		remove-deletion: completely remove it from database
#
#   for the mark-deletion policy, the convention is to have a delete_time field which not only marks up the deletion
#	but also the deletion time
#
#

SET foreign_key_checks = 0;

ALTER TABLE eh_messages MODIFY `context_type` varchar(32);
ALTER TABLE eh_messages MODIFY `context_token` varchar(32);

ALTER TABLE eh_messages CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_servers CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_shards CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_server_shard_map CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_content_shard_map CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_configurations CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_message_boxs CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acls CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acl_privileges CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acl_roles CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_acl_role_assignments CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_apps CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_app_profiles CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_sequences CONVERT TO CHARACTER SET utf8mb4;
ALTER TABLE eh_namespaces CONVERT TO CHARACTER SET utf8mb4;

SET foreign_key_checks = 1;
