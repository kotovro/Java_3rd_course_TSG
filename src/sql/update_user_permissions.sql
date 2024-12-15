CREATE OR REPLACE PROCEDURE update_user_permissions (
    IN r_id int,
	IN p_id int,
	IN pl_id int
) LANGUAGE plpgsql AS $$
BEGIN
	update role_permission set permission_level_id = pl_id where role_id = r_id and p_id = permission_name_id;
    IF not found THEN
        insert into role_permission (permission_name_id, permission_level_id, role_id) 
		values (p_id, pl_id, r_id);
    END IF;
END;
$$