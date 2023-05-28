package team3.meowie.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team3.meowie.member.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	Role findByName(String name);
}
