package gift.user.controller;

import gift.common.exception.NoSuchIdException;
import gift.user.domain.User;
import gift.user.dto.UserPatchRequestDto;
import gift.user.dto.UserSaveRequestDto;
import gift.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/admin/user")
public class UserAdminViewController {

    private final UserService userService;

    public UserAdminViewController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("userSaveRequestDto", new UserSaveRequestDto());
        return "userAddForm";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute UserSaveRequestDto userSaveRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userAddForm";
        }
        userService.createUser(userSaveRequestDto);
        return "redirect:/api/admin/user/list";
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        UserPatchRequestDto userPatchRequestDto = new UserPatchRequestDto(user);
        model.addAttribute("userPatchRequestDto", userPatchRequestDto);
        return "userUpdateForm";
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable Long id, @Valid @ModelAttribute UserPatchRequestDto userPatchRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userUpdateForm";
        }
        userService.updateUser(id, userPatchRequestDto);
        return "redirect:/api/admin/user/list";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/api/admin/user/list";
    }

}
