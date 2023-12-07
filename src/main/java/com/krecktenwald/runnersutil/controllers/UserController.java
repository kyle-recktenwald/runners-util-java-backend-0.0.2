package com.krecktenwald.runnersutil.controllers;

import com.krecktenwald.runnersutil.domain.dto.mapper.DTOMapper;
import com.krecktenwald.runnersutil.domain.dto.mapper.impl.UserDTO;
import com.krecktenwald.runnersutil.domain.entities.Route;
import com.krecktenwald.runnersutil.domain.entities.Run;
import com.krecktenwald.runnersutil.domain.entities.User;
import com.krecktenwald.runnersutil.repositories.UserRepository;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;

  @Autowired DTOMapper dtoMapper;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public Set<UserDTO> getUsers() {
    Set<UserDTO> userDTOs = new HashSet<>();
    for (User user : userRepository.findAll()) {
      userDTOs.add(convertUserToDTO(user));
    }

    return userDTOs;
  }

  @GetMapping("/{id}")
  public UserDTO getUser(@PathVariable String id) {
    return convertUserToDTO(userRepository.findById(id).orElseThrow(RuntimeException::new));
  }

  @PostMapping
  public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO)
      throws URISyntaxException {
    User user = dtoMapper.userDTOToUser(userDTO);
    user.setUserId(String.format("user_%s", UUID.randomUUID()));
    user.setCreateDate(new Date());

    UserDTO savedUserDTO = convertUserToDTO(userRepository.save(user));

    return ResponseEntity.created(new URI("/api/users/" + savedUserDTO.getUserId()))
        .body(savedUserDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
    User currentUser = userRepository.findById(id).orElseThrow(RuntimeException::new);

    currentUser.setUpdateDate(new Date());
    currentUser = userRepository.save(currentUser);
    return ResponseEntity.ok(convertUserToDTO(currentUser));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<UserDTO> deleteUser(@PathVariable String id) {
    userRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  private UserDTO convertUserToDTO(User user) {
    UserDTO userDTO = dtoMapper.userToUserDTO(user);

    Set<String> runIDs = new HashSet<>();
    if (user.getRuns() != null) {
      for (Run run : user.getRuns()) {
        runIDs.add(run.getRunId());
      }
    }
    userDTO.setRuns(runIDs);

    Set<String> createdRouteIDs = new HashSet<>();
    if (user.getRoutes() != null) {
      for (Route route : user.getRoutes()) {
        createdRouteIDs.add(route.getRouteId());
      }
    }
    userDTO.setRoutes(createdRouteIDs);
    return userDTO;
  }
}
