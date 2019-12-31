import random
class ListHierarchy:
    def __init__(self):
        self.sentinel = self.Node("")
        self.tail = None

    class Node:
        def __init__(self, key):
            self.key = key
            self.next = None
            self.prev = None
            self.up = None
            self.down = None

    '''
    addKey method will be called by add method whenever it is
    invoked to add a new element in the sorted list.
    '''
    def addKey(self, sent, addedNode, key):
        flag = False
        try:
            if self.sentinel.next is None or addedNode is None:
                if self.sentinel.next is None:
                    self.sentinel.next = self.Node(key)
                    self.tail = self.sentinel.next
                    self.tail.prev = self.sentinel
                    addedNode = self.tail
                    flag = True

                elif key.lower() > self.tail.key.lower():
                    self.tail.next = self.Node(key)
                    self.tail.next.prev = self.tail
                    self.tail = self.tail.next
                    addedNode = self.tail
                    flag = True

                else:
                    current = self.sentinel.next
                    while True:
                        if key.lower() < current.key.lower():
                            temp = self.Node(key)
                            temp.next = current
                            temp.prev = current.prev
                            current.prev = temp
                            temp.prev.next = temp
                            flag = True
                            addedNode = temp
                            break

                        else:
                            current = current.next
                            if current is None:
                                break
                coinFlipOutcome = random.getrandbits(1)
                if coinFlipOutcome == 1:
                    self.addKey(self.sentinel, addedNode, key)
            else:
                newSent = sent.up
                newAddedNode = addedNode.up
                if newSent is None:
                    newSent = self.Node("")
                    newSent.down = sent
                    sent.up = newSent

                    newAddedNode = self.Node(key)
                    newAddedNode.down = addedNode
                    addedNode.up = newAddedNode

                    newSent.next = newAddedNode
                    newAddedNode.prev = newSent
                    flag = True

                else:
                    current = newSent
                    while flag is not True:
                        if key.lower() < current.key.lower():
                            temp = self.Node(key)
                            temp.next = current
                            temp.prev = current.prev
                            current.prev = temp
                            temp.prev.next = temp

                            newAddedNode = temp
                            newAddedNode.down = addedNode
                            flag = True

                        else:
                            if current.next is None:
                                temp = self.Node(key)
                                current.next = temp
                                temp.prev = current

                                newAddedNode = temp
                                newAddedNode.down = addedNode
                                flag = True
                            current = current.next
                coinFlipOutcome = random.getrandbits(1)
                if coinFlipOutcome == 1:
                    self.addKey(newSent, newAddedNode, key)
        except:
            print("There was an exception while adding the new element")
        return flag

    '''
    The following method will be called whenever the user wants to add an element
    in the sorted string list.This is just a wrapper function that further calls
    addKey method that performs the actual add operation.
    '''
    def add(self, key):
        addedNode = None
        flag = False
        try:
            if key.isspace() or key == "":
                print("Element cannot be blank")
                return flag
            if key is None:
                print("Element cannot be None")
                return flag
            if self.sentinel.next is None:
                flag = self.addKey(self.sentinel, addedNode, key)
            else:
                isPresent = self.find(key)
                if isPresent:
                    print("The element already exists")
                else:
                    flag = self.addKey(self.sentinel, addedNode, key)
        except:
            print("Exception while adding the new element")
        return flag

    '''
    The following method can be used to check whether an element is present in
    the list or not. If the element is present it returns 'true', else 'false'.
    '''
    def find(self, key):
        flag = False
        try:
            current = self.sentinel.next
            if current is None:
                print("Structure is empty")
            else:
                while current is not None:
                    if key.lower() == current.key.lower():
                        flag = True
                        break
                    current = current.next
        except:
            print("Exception while finding the element in the structure")
        return flag

    '''
    Method to print the entire structure on the output screen.
    '''
    def printStructure(self, sent):
        if sent is None:
            return
        s = sent
        current = s.next
        print("\n")
        while current is not None:
            print("-> ", current.key, end = "\t")
            current = current.next
        self.printStructure(s.up)

    '''
    The wrapper function display can be invoked in order to display the elements
    of the structure. The actual display operation is done by displayStructure.
    '''
    def print(self):
        if self.sentinel.next is None:
            print("Structure is empty")
        else:
            self.printStructure(self.sentinel)
