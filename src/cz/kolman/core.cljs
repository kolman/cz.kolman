(ns cz.kolman.core
  (:require [rum.core :as rum]
            [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [ajax.core :as ajax]))

(enable-console-print!)

(println
  "This text is printed from src/cz.kolman/core.cljs. Go ahead and edit it and see reloading in action.")

;; routing

(def routes ["/" {"" :home, "about" :about}])

(defonce page (atom :home))

(defn- parse-url [url] (bidi/match-route routes url))

(defn- dispatch-route
  [matched-route]
  (let [panel-name (keyword (str (name (:handler matched-route))))]
    (reset! page panel-name)))

(defn app-routes [] (pushy/start! (pushy/pushy dispatch-route parse-url)))

(def url-for (partial bidi/path-for routes))

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

;; views

(defonce response (atom nil))

(defn send-request
  []
  (ajax/POST "/api/echo" {:params {:body "foo bar"}}
             :handler #(.log js/console %)))

(rum/defc echo
  []
  [:div [:input {:type "text"}] [:button {:on-click send-request} "send"]
   [:div [:p "result:"]]])

(rum/defcs navbar
  < (rum/local false ::open)
  [state]
  (let [active? (::open state)
        active-class (when @active? "is-active")]
    [:nav.navbar.is-spaced.has-shadow
     {:role "navigation", :aria-label "main navigation"}
     [:.container
      [:.navbar-brand
       [:a.navbar-item.logo.is-size-4 {:href (url-for :home)} "KOLMAN.cz"]
       [:a.navbar-burger
        {:role "button",
         :class [active-class],
         :on-click (fn [_] (swap! active? not)),
         :aria-label "menu",
         :aria-expanded false} [:span {:aria-hidden true}]
        [:span {:aria-hidden true}] [:span {:aria-hidden true}]]]
      [:.navbar-menu {:id "navbarMenu", :class [active-class]}
       [:.navbar-start [:a.navbar-item {:href (url-for :home)} "Home"]
        [:a.navbar-item {:href (url-for :about)} "About"]]
       [:.navbar-end [:.navbar-item "Say hello"]]]]]))

(rum/defc chrome
  [& content]
  [(navbar) [:section.section [:main.container content]]])

(rum/defc hello-world
  []
  (chrome [:h1.title (:text @app-state)]
          [:h3.subtitle "Welcome to brand new website!"]
          (echo)))

(rum/defc about
  []
  (chrome [:h1.title (:text @app-state)]
          [:h3.subtitle
           "This is an experimental website to test new technologies"]))

(rum/defc app
  < rum/reactive
  []
  (case (rum/react page)
    :home (hello-world)
    :about (about)))

(app-routes)

(rum/mount (app) (. js/document (getElementById "app")))

(defn on-js-reload
  []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
