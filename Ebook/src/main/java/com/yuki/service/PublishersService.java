package com.yuki.service;

import com.yuki.dto.PublishersDTO;
import com.yuki.entity.Publisher;
import com.yuki.repositoty.PublisherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublishersService {
    @Autowired
    private PublisherDAO publisherDAO;

    public Publisher createPublisher(PublishersDTO publisherDTO) {
        Publisher publisher = new Publisher();
        publisher.setPublisherName(publisherDTO.getPublisherName());
        publisher.setStatus(true);
        return publisherDAO.save(publisher);
    }
    public Publisher updatePublisher(int ID, PublishersDTO publisherDTO) {
        Optional<Publisher> publisher = publisherDAO.findById(ID);
        if (publisher.isPresent()) {
            Publisher publisherUP = publisher.get();
            publisherUP.setPublisherName(publisherDTO.getPublisherName());
            publisherUP.setStatus(true);
            return publisherDAO.save(publisherUP);
        }
        return null;
    }

    public void deletePublisher(int ID) {
        Optional<Publisher> publisher = publisherDAO.findById(ID);
        if (publisher.isPresent()) {
            Publisher deletePublisher = publisher.get();
            deletePublisher.setStatus(false);
            publisherDAO.save(deletePublisher);
        }
    }
}
