package sg.edu.iss.caps.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.iss.caps.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

	@Query("select a from Admin a where a.username like %?1%")
	List<Admin> searchAdminByName(String name);
	
	@Query("select a from Admin a where a.username = ?1 and a.password = ?2")
	List<Admin> searchAdminByCredentials(String username, String password);

}
