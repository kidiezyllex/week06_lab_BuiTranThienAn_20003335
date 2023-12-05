package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.models.User;
import vn.edu.iuh.fit.backend.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User insert(User user){
        return userRepository.save(user);
    }
    public User update(User user){
        return userRepository.save(user);
    }
    public void deleteById(long id){
        userRepository.deleteById(id);
        return;
    }
    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }
    public Page<User> findAll(int pageNo, int sizeNo, String sortBy, String sortDerection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDerection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, sizeNo, sort);
        return userRepository.findAll(pageable);
    }
}
