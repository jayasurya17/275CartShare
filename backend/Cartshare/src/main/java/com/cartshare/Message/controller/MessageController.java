package com.cartshare.Message.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.cartshare.Message.dao.MessageDAO;
import com.cartshare.User.dao.UserDAO;
import com.cartshare.models.Message;
import com.cartshare.models.User;
import com.cartshare.utils.MailController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageDAO messageDAO;
    
    @Autowired
    UserDAO userDAO;

    @PostMapping(value="/{from}/{to}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> createMessage(@Valid
                                        @PathVariable(name = "from") String from,
                                        @PathVariable(name = "to") String to,
                                        @RequestParam(name = "message") String message){
                                            
        if(from == null || to == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        if(from.length() == 0 || to.length() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        if (!userDAO.screenNameExists(from) || !userDAO.screenNameExists(to)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        }
        if(from.equals(to))
        	 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot send messages to yourself");
        	
        if(message == null || message.length() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid message");

        User userFrom = userDAO.findByScreenName(from);
        User userTo = userDAO.findByScreenName(to);
        
        MailController mc = new MailController();

        String subject = "A message from " + from + " via CartShare!";

        boolean s = mc.send(userTo.getEmail(), subject, message);

        if(s){
            Message m = new Message(userFrom.getId(), userTo.getId(), message, new Date());
            messageDAO.save(m);
            return ResponseEntity.status(HttpStatus.OK).body("Message sent successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message cuold not be sent");
        }
        
    }


    @GetMapping(value="/{from}/{to}", produces = { "application/json", "application/xml" })
    public ResponseEntity<?> getMessages(@Valid
                                        @PathVariable(name = "from") String from,
                                        @PathVariable(name = "to") String to){
                            
        if(from == null || to == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        if(from.length() == 0 || to.length() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid user ID");
        if (!userDAO.screenNameExists(from) || !userDAO.screenNameExists(to)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }

        User userFrom = userDAO.findByScreenName(from);
        User userTo = userDAO.findByScreenName(to);

        List<Message> l = messageDAO.findByFromAndTo(userFrom.getId(), userTo.getId());

        if(l == null || l.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Messages Found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(l);

    }

}