package com.krecktenwald.runnersutil.domain.dto.mapper;

import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RoleDTO;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RouteDTO;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.RunDTO;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.UserDTO;
import com.krecktenwald.runnersutil.domain.entities.Role;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.domain.entities.User;
import java.util.List;
import java.util.Set;

/** * Maps POJO/Entity objects to Data Transfer Objects (DTOs), and vice-versa */
public interface DTOMapper {

  // Entity to DTO mappings
  RoleDTO roleToRoleDTO(Role role);

  UserDTO userToUserDTO(User user);

  RouteDTO routeToRouteDTO(Route route);

  RunDTO runToRunDTO(Run run);

  // DTO to Entity mappings
  Role roleDTOToRole(RoleDTO roleDTO);

  User userDTOToUser(UserDTO userDTO);

  Route routeDTOToRoute(RouteDTO routeDTO);

  Run runDTOToRun(RunDTO runDTO);

  // Mapping for Set conversions
  Set<RoleDTO> rolesToRoleDTOs(Set<Role> roles);

  Set<UserDTO> usersToUserDTOs(Set<User> users);

  Set<RouteDTO> routesToRouteDTOs(Set<Route> routes);

  Set<RunDTO> runsToRunDTOs(Set<Run> runs);

  Set<Role> roleDTOsToRoles(Set<RoleDTO> roleDTOs);

  Set<User> userDTOsToUsers(Set<UserDTO> userDTOs);

  Set<Route> routeDTOsToRoutes(Set<RouteDTO> routeDTOs);

  Set<Run> runDTOsToRuns(Set<RunDTO> runDTOs);

  // Mapping for List conversions
  List<RoleDTO> rolesToRoleDTOs(List<Role> roles);

  List<UserDTO> usersToUserDTOs(List<User> users);

  List<RouteDTO> routesToRouteDTOs(List<Route> routes);

  List<RunDTO> runsToRunDTOs(List<Run> runs);

  List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs);

  List<User> userDTOsToUsers(List<UserDTO> userDTOs);

  List<Route> routeDTOsToRoutes(List<RouteDTO> routeDTOs);

  List<Run> runDTOsToRuns(List<RunDTO> runDTOs);
}
