package pl.szymanski.user.service.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.szymanski.user.service.dto.RoleDTO;
import pl.szymanski.user.service.facade.RoleFacade;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles operations")
public class RoleController {

	@Autowired
	private RoleFacade roleFacade;

	@GetMapping
	@Operation(summary = "Retrieves all roles list")
	@ApiResponse(responseCode = "200", description = "List of all roles in system", content = @Content(schema = @Schema(implementation = RoleDTO.class)))
	public @ResponseBody ResponseEntity<List<RoleDTO>> roles() {
		List<RoleDTO> roles = roleFacade.getAllRoles();
		return ResponseEntity.status(HttpStatus.OK).body(roles);
	}
}
