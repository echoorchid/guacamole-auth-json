/*
 * Copyright (C) 2015 Glyptodon LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.glyptodon.guacamole.auth.json.user;

import com.google.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.form.Form;
import org.apache.guacamole.net.auth.ActiveConnection;
import org.apache.guacamole.net.auth.ActivityRecord;
import org.apache.guacamole.net.auth.ActivityRecordSet;
import org.apache.guacamole.net.auth.AuthenticationProvider;
import org.apache.guacamole.net.auth.Connection;
import org.apache.guacamole.net.auth.ConnectionGroup;
import org.apache.guacamole.net.auth.ConnectionRecord;
import org.apache.guacamole.net.auth.Directory;
import org.apache.guacamole.net.auth.SharingProfile;
import org.apache.guacamole.net.auth.User;
import org.apache.guacamole.net.auth.simple.SimpleActivityRecordSet;
import org.apache.guacamole.net.auth.simple.SimpleDirectory;

/**
 * An implementation of UserContext specific to the JSONAuthenticationProvider
 * which obtains all data from the encrypted JSON provided during
 * authentication.
 *
 * @author Michael Jumper
 */
public class UserContext implements org.apache.guacamole.net.auth.UserContext {

    /**
     * Reference to the AuthenticationProvider associated with this
     * UserContext.
     */
    @Inject
    private AuthenticationProvider authProvider;

    /**
     * Service for deriving Guacamole extension API data from UserData objects.
     */
    @Inject
    private UserDataService userDataService;

    /**
     * The UserData object associated with the user to whom this UserContext
     * belongs.
     */
    private UserData userData;

    /**
     * Initializes this UserContext using the data associated with the provided
     * UserData object.
     *
     * @param userData
     *     The UserData object derived from the JSON data received when the
     *     user authenticated.
     */
    public void init(UserData userData) {
        this.userData = userData;
    }

    @Override
    public User self() {
        return userDataService.getUser(userData);
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authProvider;
    }

    @Override
    public Object getResource() {
        return null;
    }

    @Override
    public Directory<User> getUserDirectory() throws GuacamoleException {
        return userDataService.getUserDirectory(userData);
    }

    @Override
    public Directory<Connection> getConnectionDirectory() {
        return userDataService.getConnectionDirectory(userData);
    }

    @Override
    public Directory<ConnectionGroup> getConnectionGroupDirectory() {
        return userDataService.getConnectionGroupDirectory(userData);
    }

    @Override
    public Directory<SharingProfile> getSharingProfileDirectory()
            throws GuacamoleException {
        return new SimpleDirectory<SharingProfile>();
    }

    @Override
    public ConnectionGroup getRootConnectionGroup() throws GuacamoleException {
        return userDataService.getRootConnectionGroup(userData);
    }

    @Override
    public Directory<ActiveConnection> getActiveConnectionDirectory()
            throws GuacamoleException {
        return new SimpleDirectory<ActiveConnection>();
    }

    @Override
    public ActivityRecordSet<ConnectionRecord> getConnectionHistory()
            throws GuacamoleException {
        return new SimpleActivityRecordSet<ConnectionRecord>();
    }

    @Override
    public ActivityRecordSet<ActivityRecord> getUserHistory()
            throws GuacamoleException {
        return new SimpleActivityRecordSet<ActivityRecord>();
    }

    @Override
    public Collection<Form> getUserAttributes() {
        return Collections.<Form>emptyList();
    }

    @Override
    public Collection<Form> getConnectionAttributes() {
        return Collections.<Form>emptyList();
    }

    @Override
    public Collection<Form> getConnectionGroupAttributes() {
        return Collections.<Form>emptyList();
    }

    @Override
    public Collection<Form> getSharingProfileAttributes() {
        return Collections.<Form>emptyList();
    }

    @Override
    public void invalidate() {
        // Nothing to invalidate
    }

}
