package org.yearup.data;

import org.yearup.models.Profile;

public interface ProfileDao
{
    void create(Profile profile);
    Profile getByUserId(int userId);
    void update(Profile profile);
    void delete(int userId);

}