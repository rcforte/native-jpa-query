package com.person.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
class PersonRestController {
  private final PersonRepository personRepository;

  public PersonRestController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @GetMapping("/")
  List<Person> all() {
    return personRepository.findAll();
  }

  @GetMapping("/{id}")
  Person byId(@PathVariable("id") Long id) {
    return personRepository.findById(id).get();
  }

  @GetMapping("/native/{id}")
  Person byIdNative(@PathVariable Long id) {
    return personRepository.foo(id);
  }
}

class BTree {
  private Node root;
  private int count;
  private int height;

  public BTree() {
    root = new Node(0);
  }

  public void put(Integer key, String value) {
    this.count++;
    var newNode = insert(root, key, value, height);
    if (newNode == null) {
      return;
    }

    var newRoot = new Node(2);
    newRoot.entries[0] = new Entry(root.entries[0].key, null, root);
    newRoot.entries[1] = new Entry(newNode.entries[0].key, null, newNode);
    this.root = newRoot;
    this.height++;
  }

  private Node insert(Node node, Integer key, String value, int height) {
    var newEntry = new Entry(key, value, null);
    var position = 0;
    if (height == 0) {
      for (position = 0; position < node.count; position++) {
        if (key < node.entries[position].key) {
          break;
        }
      }
    }
    else {
      for (position = 0; position < node.count; position++) {
        if (position + 1 == node.count || key < node.entries[position + 1].key) {
          var newNode = insert(node.entries[position++].next, key, value, height - 1);
          if (newNode == null) {
            return null;
          }
          newEntry.key = newNode.entries[0].key;
          newEntry.value = null;
          newEntry.next = newNode;
          break;
        }
      }
    }

    for (var i = node.count; i > position; i--) {
      node.entries[i] = node.entries[i - 1];
    }
    node.entries[position] = newEntry;
    node.count++;
    return node.count == Node.MAX_ENTRIES
        ? split(node)
        : null;
  }

  private Node split(Node node) {
    var half = Node.MAX_ENTRIES / 2;
    node.count = half;
    var result = new Node(half);
    for (var i = 0; i < half; i++) {
      result.entries[i] = node.entries[half + i];
    }
    return result;
  }

  static class Entry {
    private Integer key;
    private String value;
    private Node next;

    Entry(Integer key, String value, Node next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }

  static class Node {
    private static final int MAX_ENTRIES = 4;

    private Entry[] entries = new Entry[MAX_ENTRIES];
    private int count;

    public Node(int count) {
      this.count = count;
    }
  }
}