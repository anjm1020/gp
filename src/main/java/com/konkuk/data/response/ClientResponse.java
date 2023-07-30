package com.konkuk.data.response;

public class ClientResponse {
    private String script;
    private boolean active;
    private String type;
    private boolean finish;
    private int dialog_id;

    public static ClientResponse getTestResponse() {
        return ClientResponse.builder()
                .script("TEST SCRIPT")
                .active(false)
                .type("my")
                .finish(true)
                .dialog_id(1).build();
    }
    public static Builder builder() {
        return new Builder();
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getDialog_id() {
        return dialog_id;
    }

    public void setDialog_id(int dialog_id) {
        this.dialog_id = dialog_id;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ClientResponse)) return false;
        ClientResponse o1 = (ClientResponse) obj;
        return this.script.equals(o1.script) &&
                this.active == o1.active &&
                this.type.equals(o1.type) &&
                this.finish == o1.finish &&
                this.dialog_id == o1.dialog_id;
    }

    public static class Builder {
        private String script;
        private boolean active;
        private String type;
        private boolean finish;
        private int dialog_id;

        public ClientResponse build() {
            ClientResponse res = new ClientResponse();
            res.setScript(script);
            res.setActive(active);
            res.setFinish(finish);
            res.setType(type);
            res.setDialog_id(dialog_id);
            return res;
        }

        public Builder script(String script) {
            this.script = script;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder finish(boolean finish) {
            this.finish = finish;
            return this;
        }
        public Builder dialog_id(int dialog_id) {
            this.dialog_id = dialog_id;
            return this;
        }
    }
}
