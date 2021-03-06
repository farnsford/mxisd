/*
 * mxisd - Matrix Identity Server Daemon
 * Copyright (C) 2017 Kamax Sarl
 *
 * https://www.kamax.io/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.kamax.mxisd.config;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties("session")
public class SessionConfig {

    private static Logger log = LoggerFactory.getLogger(SessionConfig.class);

    public static class Policy {

        public static class PolicyTemplate {

            public static class PolicySource {

                public static class PolicySourceRemote {

                    private boolean enabled;
                    private String server;

                    public boolean isEnabled() {
                        return enabled;
                    }

                    public void setEnabled(boolean enabled) {
                        this.enabled = enabled;
                    }

                    public String getServer() {
                        return server;
                    }

                    public void setServer(String server) {
                        this.server = server;
                    }

                }

                private boolean enabled;
                private boolean toLocal;
                private PolicySourceRemote toRemote = new PolicySourceRemote();

                public boolean isEnabled() {
                    return enabled;
                }

                public void setEnabled(boolean enabled) {
                    this.enabled = enabled;
                }

                public boolean toLocal() {
                    return toLocal;
                }

                public void setToLocal(boolean toLocal) {
                    this.toLocal = toLocal;
                }

                public boolean toRemote() {
                    return toRemote.isEnabled();
                }

                public PolicySourceRemote getToRemote() {
                    return toRemote;
                }

                public void setToRemote(PolicySourceRemote toRemote) {
                    this.toRemote = toRemote;
                }

            }

            private boolean enabled;
            private PolicySource forLocal = new PolicySource();
            private PolicySource forRemote = new PolicySource();

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public PolicySource getForLocal() {
                return forLocal;
            }

            public PolicySource forLocal() {
                return forLocal;
            }

            public PolicySource getForRemote() {
                return forRemote;
            }

            public PolicySource forRemote() {
                return forRemote;
            }

            public PolicySource forIf(boolean isLocal) {
                return isLocal ? forLocal : forRemote;
            }

        }

        private PolicyTemplate validation = new PolicyTemplate();

        public PolicyTemplate getValidation() {
            return validation;
        }

        public void setValidation(PolicyTemplate validation) {
            this.validation = validation;
        }

    }

    private MatrixConfig mxCfg;
    private Policy policy = new Policy();

    @Autowired
    public SessionConfig(MatrixConfig mxCfg) {
        this.mxCfg = mxCfg;
    }

    public MatrixConfig getMatrixCfg() {
        return mxCfg;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    @PostConstruct
    public void build() {
        log.info("--- Session config ---");
        log.info("Global Policy: {}", new Gson().toJson(policy));
    }

}
