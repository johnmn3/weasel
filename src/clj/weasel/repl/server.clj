(ns weasel.repl.server
  (:import [java.nio ByteBuffer]
           [java.io BufferedReader IOException]
           [org.java_websocket.client WebSocketClient]
           [org.java_websocket.server WebSocketServer]))


(defn ws-server-impl [host port open error close str-msg bb-msg start]
  (proxy [WebSocketServer] [(java.net.InetSocketAddress. host port #_ (read-string port))]
    (onOpen [client client-handshake]
      (open {:client client :client-handshake client-handshake}))
    (onClose [client code reason remote]
      (close {:client client :code code :reason reason :remote remote})
      (.close client))
    (onMessage [client msg]
      (condp instance? msg
        String (str-msg {:client client :msg msg})
        ByteBuffer (bb-msg {:client client :msg msg})))
    (onError [client ex]
      (error {:client client :ex ex}))
    (onStart []
      (when start
        (start)))))

;; state
(defonce state
  (atom {:server nil
         :clients nil
         :started nil}))

(defn server [host port & args]
  (let [{:keys [open error close str-msg bb-msg start]
         :or {close (fn [{:keys [client]}]
                      (swap! state update :clients #(remove #{client} %)))
              open (fn [{:keys [client]}]
                     (-> (swap! state update :clients conj
                            {:ws client :id (gensym "client")})
                      :started (deliver true)))
              str-msg (fn [{:keys [msg]}] (println "from client:" msg))
              bb-msg str-msg
              error (fn [{:keys [client ex]}] (println client "sent error:" ex))}}

        (apply hash-map args)
        ws (ws-server-impl host port open error close str-msg bb-msg start)]
    (future (.run ws))
    ws))

(defn send!
  ([msg]
   (if-let [ws (-> (:clients @state) first :ws)]
     (send! ws msg)))
  ([client msg]
   (.send client (pr-str msg))))

(defn start
  [f & {:keys [ip port]}]
  {:pre [(ifn? f)]}
  (swap! state
    assoc :server (server ip port :str-msg f)
          :clients #{}
          :started (promise)))

(defn stop []
  (let [stop-server (:server @state)]
    (when-not (nil? stop-server)
      (.stop stop-server)
      (reset! state {:server nil
                     :clients nil
                     :started nil})
      @state)))

(defn wait-for-client []
  (deref (:started @state))
  nil)

(defn restart []
  (stop)
  (start {}))
