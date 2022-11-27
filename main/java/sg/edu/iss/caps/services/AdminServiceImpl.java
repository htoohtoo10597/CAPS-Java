package sg.edu.iss.caps.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.caps.model.Admin;
import sg.edu.iss.caps.repositories.AdminRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminrepo;
	
	@Override
	public List<Admin> getAllAdmin() {
		// TODO Auto-generated method stub
		return adminrepo.findAll();
	}

	@Override
	public Admin getAdminById(String id) {
		// TODO Auto-generated method stub
		Optional<Admin> optional = adminrepo.findById(id);
		Admin admin = null;

		if(optional.isPresent()){
			admin = optional.get();
		} else {
			throw new RuntimeException("Admin not found.");
		}

		return admin;
	}

	@Override
	public List<Admin> returnAdminByCredentials(String username, String password) {
		// TODO Auto-generated method stub
		return adminrepo.searchAdminByCredentials(username, password);
	}

}
