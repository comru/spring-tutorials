package com.sbu.dj.domain.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public Set<Tag> getOrCreateTags(Collection<String> tagNames) {
        Set<Tag> result = new HashSet<>(tagRepository.findByNameIn(tagNames));

        Set<String> existTagNames = result.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existTagNames.contains(name))
                .map(tagName -> new Tag().setName(tagName)).toList();

        if (!newTags.isEmpty()) {
            result.addAll(tagRepository.saveAll(newTags));
        }

        return result;
    }
}
