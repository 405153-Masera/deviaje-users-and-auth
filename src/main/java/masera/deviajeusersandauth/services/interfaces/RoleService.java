package masera.deviajeusersandauth.services.interfaces;

import masera.deviajeusersandauth.dtos.get.RoleDto;
import masera.deviajeusersandauth.entities.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
  List<RoleDto> getAllRoles();
  RoleDto getRoleById(Integer id);
  RoleEntity getRoleByName(String name);
}
