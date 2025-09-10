package com.project.Service;

import com.project.Entity.BookSet;
import org.antlr.v4.runtime.misc.Pair;

import java.util.List;

public interface BookSetService
{
    BookSet createBookSet(Pair<BookSet,Long> pair);
    BookSet getBookSetById(Long id);
    List<BookSet> getAllBookSets();
    BookSet updateBookSet(Pair<BookSet,Long>updatedSetWithSellerId);
    void deleteBookSet(Long id);
}
