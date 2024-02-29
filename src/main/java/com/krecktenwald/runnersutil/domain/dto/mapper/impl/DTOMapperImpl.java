package com.krecktenwald.runnersutil.domain.dto.mapper.impl;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.entities.Permission;
import com.krecktenwald.runnersutil.domain.entities.Role;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.domain.entities.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DTOMapperImpl implements DTOMapper {

  @Override
  public RoleDTO roleToRoleDTO(Role role) {
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setRoleId(role.getRoleId());
    roleDTO.setName(role.getName());
    roleDTO.setDescription(role.getDescription());
    roleDTO.setPermissions(mapPermissionSetToStrings(role.getPermissions()));

    return roleDTO;
  }

  @Override
  public UserDTO userToUserDTO(User user) {
    UserDTO userDTO = new UserDTO();
    userDTO.setUserId(user.getUserId());
    userDTO.setAuthId(user.getAuthId());
    userDTO.setEmailAddress(user.getEmailAddress());

    return userDTO;
  }

  @Override
  public RouteDTO routeToRouteDTO(Route route) {
    RouteDTO routeDTO = new RouteDTO();
    routeDTO.setRouteId(route.getRouteId());
    routeDTO.setName(route.getName());
    routeDTO.setDistance(route.getDistance());

    return routeDTO;
  }

  @Override
  public RunDTO runToRunDTO(Run run) {
    RunDTO runDTO = new RunDTO();
    runDTO.setRunId(run.getRunId());
    runDTO.setDistance(run.getDistance());
    runDTO.setStartDateTime(run.getStartDateTime());
    runDTO.setDuration(run.getDuration());

    return runDTO;
  }

  @Override
  public Role roleDTOToRole(RoleDTO roleDTO) {
    return null;
  }

  @Override
  public User userDTOToUser(UserDTO userDTO) {
    return null;
  }

  @Override
  public Route routeDTOToRoute(RouteDTO routeDTO) {
    return null;
  }

  @Override
  public Run runDTOToRun(RunDTO runDTO) {
    return null;
  }

  @Override
  public Set<RoleDTO> rolesToRoleDTOs(Set<Role> roles) {
    return null;
  }

  @Override
  public Set<UserDTO> usersToUserDTOs(Set<User> users) {
    return null;
  }

  @Override
  public Set<RouteDTO> routesToRouteDTOs(Set<Route> routes) {
    return null;
  }

  @Override
  public Set<RunDTO> runsToRunDTOs(Set<Run> runs) {
    return null;
  }

  @Override
  public Set<Role> roleDTOsToRoles(Set<RoleDTO> roleDTOs) {
    return null;
  }

  @Override
  public Set<User> userDTOsToUsers(Set<UserDTO> userDTOs) {
    return null;
  }

  @Override
  public Set<Route> routeDTOsToRoutes(Set<RouteDTO> routeDTOs) {
    return null;
  }

  @Override
  public Set<Run> runDTOsToRuns(Set<RunDTO> runDTOs) {
    return null;
  }

  @Override
  public List<RoleDTO> rolesToRoleDTOs(List<Role> roles) {
    return null;
  }

  @Override
  public List<UserDTO> usersToUserDTOs(List<User> users) {
    return null;
  }

  @Override
  public List<RouteDTO> routesToRouteDTOs(List<Route> routes) {
    return null;
  }

  @Override
  public List<RunDTO> runsToRunDTOs(List<Run> runs) {
    return null;
  }

  @Override
  public List<Role> roleDTOsToRoles(List<RoleDTO> roleDTOs) {
    return null;
  }

  @Override
  public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
    return null;
  }

  @Override
  public List<Route> routeDTOsToRoutes(List<RouteDTO> routeDTOs) {
    return null;
  }

  @Override
  public List<Run> runDTOsToRuns(List<RunDTO> runDTOs) {
    return null;
  }

  public static Set<String> mapPermissionSetToStrings(Set<Permission> permissions) {
    return permissions.stream()
        .map(Permission::name) // Assuming Permission is an enum with a 'name' method
        .collect(Collectors.toSet());
  }
}
