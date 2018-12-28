package org.lz.boilerplate.springrest.service;

import org.lz.boilerplate.springrest.repository.UserRepository;
import org.lz.boilerplate.springrest.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class DaoUserDetailServices implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var user = userRepository.findByName(name).stream().findFirst().orElse(null);
        if(user == null) {
            return null;
        }
        return new UserPrincipal(user.getName(), user.getPassword(), user.getRole());
    }
}
