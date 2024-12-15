CREATE OR REPLACE PROCEDURE delete_role(
    r_id INT
    )
LANGUAGE plpgsql
AS $$
BEGIN
    
    delete from user_role
   	where role_id = r_id;

    delete from role_permission
   	where role_id = r_id;
	
	delete from "role"
   	where role_id = r_id;

    

    RAISE NOTICE 'Delete all infromation about rolw ith ID: %', r_id;
END;
$$;