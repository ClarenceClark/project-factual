(ns project-factual.views.suggestions-box
  (:require [reagent.core :as reagent]))

(defn- index-wrap [min-i max-i new-index]
  (cond
    (< max-i new-index) min-i
    (> min-i new-index) max-i
    :else new-index))

(defn- select-suggestion-by-index [state index]
  (assoc state :active-suggestion-index index))

(defn- modify-selection-index-by [state func]
  (let [{:keys [active-suggestion-index suggestions]} state]
    (select-suggestion-by-index
      state
      (index-wrap
        0
        ; Since index is 0-indexed, but count is 1-based, we need to dec for max
        (dec (count suggestions))
        (func active-suggestion-index)))))

(defn- show-suggestions [state show?]
  (assoc state :show-suggestions? show?))

(defn- update-internal-input-state [state newval]
  (assoc state :search newval))

(defn- get-new-suggestions [state]
  (let [{:keys [data-source search]} state]
    (assoc state :suggestions (data-source search))))

(defn- set-suggestion! [state-atom new-value]
  (let [{:keys [input-dom]} @state-atom]
    (set! (.-value input-dom) new-value)
    (swap! state-atom update-internal-input-state new-value)))

(defn- choose-suggestion! [state-atom]
  (let [{:keys [on-change suggestions active-suggestion-index]} @state-atom
        sug-count (count suggestions)
        chosen (when-not (= sug-count 0)
                 (nth suggestions active-suggestion-index))]
    (if chosen
      (do (on-change chosen)
          (set-suggestion! state-atom "")
          (swap! state-atom get-new-suggestions)))))

(defn- input-key-down! [state-atom event]
  (let [{:keys [on-blur]} @state-atom]
    (condp = (.-which event)
      ; UP
      38 (swap! state-atom modify-selection-index-by dec)
      ; DOWN
      40 (swap! state-atom modify-selection-index-by inc)
      ; ENTER
      13 (choose-suggestion! state-atom)
      ; TAB
      9 (do (choose-suggestion! state-atom)
            ; Don't lose focus
            (.preventDefault event))
      ; ESCAPE
      27 (do (.blur (.-currentTarget event))
             (on-blur))
      nil)))

(defn suggestions-box [data-source on-change render-fn & rest]
  "Input field with suggestions; based loosely on re-com's typeahead"
  (let [{:keys [on-blur]} rest
        state-atom (reagent/atom {:show-suggestions? false
                                  :search ""
                                  :suggestions (data-source "")
                                  :active-suggestion-index 0
                                  :on-change on-change
                                  :data-source data-source
                                  :input-dom nil
                                  :on-blur (or on-blur (constantly nil))})]
    (reagent/create-class
      {:component-did-mount
       (fn [this]
         (let [dom-node (reagent/dom-node this)
               input-node (.-firstElementChild dom-node)]
           (swap! state-atom #(assoc % :input-dom input-node))))
       :reagent-render
       (fn []
         (let [{:keys [show-suggestions? suggestions active-suggestion-index input-dom]} @state-atom]
           [:div {:class "suggestions-box"}
            [:input {:class "suggestion-box-input"
                     :on-focus #(do (swap! state-atom show-suggestions true)
                                    (swap! state-atom get-new-suggestions))
                     :on-blur #(do (swap! state-atom show-suggestions false)
                                   (set-suggestion! state-atom ""))
                     :on-key-down #(input-key-down! state-atom %)
                     :on-change #(do (swap! state-atom select-suggestion-by-index 0)
                                     (swap! state-atom update-internal-input-state (.-value input-dom))
                                     (swap! state-atom get-new-suggestions))}]
            [:div {:class (str "suggestions-container"
                               (when (or (not show-suggestions?)
                                         (= 0 (count suggestions)))
                                 " hide"))}
             (for [[index suggestion] (map vector (range) suggestions)
                   :let [selected? (= index active-suggestion-index)]]
               ^{:key index}
               [:div {:class (str "suggestion" (when selected? " active"))
                      ; We don't want mouse clicks to unfocus the text box
                      :on-mouse-down #(.preventDefault %)
                      :on-click #(choose-suggestion! state-atom)
                      :on-mouse-enter #(swap! state-atom select-suggestion-by-index index)}
                (render-fn suggestion)])]]))})))